package org.example;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.example.common.Either;
import org.example.common.ObjectWithException;
import org.example.model.ExceptedObject;
import org.example.model.Member;
import org.example.model.MemberRq;
import org.example.model.Role;
import org.example.repository.ExceptedObjectRepository;
import org.example.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.example.common.Either.lift;
import static org.example.source.Generator.getLong;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StreamPracticeTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ExceptedObjectRepository exceptedObjectRepository;

    @DisplayName("[try-catch] MemberRq stream 처리 중 Exception 발생")
    @ParameterizedTest
    @MethodSource(value = "org.example.source.MemberSource#getMemberRqsIncludeOdd")
    void stream_try_catch(List<MemberRq> memberRqs) {
        AtomicInteger i = new AtomicInteger(1);
        Assertions.assertThatThrownBy(() -> memberRqs.stream().map(memberRq -> {
            try {
                Member transformedMember = new Member(getLong(), memberRq.getName(), Role.valueOf(memberRq.getRole()), memberRq.getOrganization(), memberRq.getLevel());
                log.debug("[{}] transform complete!\n", i.getAndIncrement());
                return transformedMember;
            } catch (Exception e) {
                log.debug("something wrong!");
                throw new IllegalArgumentException();
            }
        }).collect(toList())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[wrap 사용] MemberRq stream 처리 중 Exception 발생")
    @ParameterizedTest
    @MethodSource(value = "org.example.source.MemberSource#getMemberRqsIncludeOdd")
    void stream_wrap(List<MemberRq> memberRqs) {
        AtomicInteger i = new AtomicInteger(1);
        Assertions.assertThatThrownBy(() -> memberRqs.stream().map(wrap(memberRq -> {
            Member transformedMember = new Member(getLong(), memberRq.getName(), Role.valueOf(memberRq.getRole()), memberRq.getOrganization(), memberRq.getLevel());
            log.debug("[{}] transform complete!\n", i.getAndIncrement());
            return transformedMember;
        })).collect(toList())).isInstanceOf(IllegalArgumentException.class);
    }

    public static <T, L> Function<T, L> wrap(Function<T, L> function) {
        return (T t) -> {
            try {
                return function.apply(t);
            } catch (Exception ex) {
                log.debug("something wrong!");
                throw new IllegalArgumentException();
            }
        };
    }

    @DisplayName("[저장 요청]MemberRq 받아서 stream 처리 중 Exception 발생하는 객체를 분리")
    @ParameterizedTest
    @MethodSource(value = "org.example.source.MemberSource#getMemberRqsIncludeOdd")
    void either_save_success(List<MemberRq> memberRqs) {
        List<Either> membersIncludeException = memberRqs.stream().map(lift(StreamPracticeTest::mapToMember)).collect(toList());

        Map<Boolean, ? extends List<?>> membersOrObjectWithException = membersIncludeException.stream().filter(Either::isNotNull).collect(groupingBy(Either::isLeft, mapping(either -> either.get().get(), toList())));

        List<Member> members = (List<Member>) membersOrObjectWithException.get(true);
        List<ExceptedObject> exceptedObjects = ((List<ObjectWithException>) membersOrObjectWithException.get(false)).stream().map(oe -> new ExceptedObject(getLong(), oe.getOrigin(), oe.getEx().getMessage())).collect(toList());

        log.debug("Excepted objects -------------------------------- ");
        for (ExceptedObject exceptedObject : exceptedObjects) {
            log.debug(exceptedObject.toString());
        }

        memberRepository.saveAll(members);
        exceptedObjectRepository.saveAll(exceptedObjects);

        assertAll(() -> Assertions.assertThat(members.size()).isEqualTo(500000 - 1), () -> Assertions.assertThat(exceptedObjects.size()).isEqualTo(1));
    }

    @DisplayName("[레벨업 요청] Members 조회 후 stream 레벨업 처리 중 Exception 발생하는 객체를 분리하여 batch update")
    @ParameterizedTest
    @MethodSource(value = "org.example.source.MemberSource#getMembersIncludeOddLevel")
    void either_levelUp_success(List<Member> inputMembers) {
        List<Either> membersIncludeException = inputMembers.stream().map(lift(Member::levelUp)).collect(toList());

        Map<Boolean, ? extends List<?>> membersOrObjectWithException = membersIncludeException.stream().filter(Either::isNotNull).collect(groupingBy(Either::isLeft, mapping(either -> either.get().get(), toList())));

        List<Member> members = (List<Member>) membersOrObjectWithException.get(true);
        List<ExceptedObject> exceptedObjects = ((List<ObjectWithException<Member>>) membersOrObjectWithException.get(false)).stream().map(oe -> new ExceptedObject(getLong(), oe.getOrigin(), oe.getEx().getMessage())).collect(toList());

        List<Long> needLevelUpMemberIds = exceptedObjects.stream().map(exceptedObject -> ((Member) exceptedObject.getOrigin()).getId()).collect(toList());

        log.debug("exception occured member ids({}): {}", needLevelUpMemberIds.size(), needLevelUpMemberIds.toString());

        memberRepository.levelUpBatchById(needLevelUpMemberIds);
    }

    private static Member mapToMember(MemberRq memberRq) {
        return Member.of(getLong(), memberRq);
    }
}