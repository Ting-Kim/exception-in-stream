package org.example.source;

import org.example.model.Member;
import org.example.model.MemberRq;
import org.example.model.Role;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.example.source.Generator.create;
import static org.example.source.Generator.getInt;
import static org.example.source.Generator.getLong;
import static org.example.source.Generator.getRandomStringRole;
import static org.junit.jupiter.params.provider.Arguments.arguments;

// org.example.source.MemberSource
public class MemberSource {

    final static int SIZE_OF_DATA = 500000;
    final static int SIZE_OF_TEST_CASE = 5;
    final static int INTERVAL_WITH_ID = 1;
    final static int MAX_LEVEL = 5;

    static Stream<Arguments> getMemberRqsIncludeOdd() {
        Stream<Arguments> stream = Stream.empty();

        for (long i = 0; i < SIZE_OF_TEST_CASE; i++) {
            List<MemberRq> memberRqs = getRandomMemberRqs();

            stream = Stream.concat(
                    stream,
                    Stream.of(arguments(memberRqs)));
        }

        return stream;
    }

    static Stream<Arguments> getMembersIncludeOddLevel() {
        Stream<Arguments> stream = Stream.empty();

        for (long i = 0; i < SIZE_OF_TEST_CASE; i++) {
            List<Member> members = getRandomMembers();

            stream = Stream.concat(
                    stream,
                    Stream.of(arguments(members)));
        }

        return stream;
    }

    private static List<MemberRq> getRandomMemberRqs() {
        List<MemberRq> members = new ArrayList<>();
        int oddIndex = getInt(SIZE_OF_DATA);

        for (long i = 0; i < SIZE_OF_DATA; i+=INTERVAL_WITH_ID) {
            if (i == oddIndex) {
                members.add(new MemberRq(create(), "TRICK", create(), getInt(MAX_LEVEL)));
                continue;
            }
            members.add(new MemberRq(create(), getRandomStringRole(), create(), getInt(MAX_LEVEL)));
        }
        return members;
    }

    private static List<Member> getRandomMembers() {
        List<Member> members = new ArrayList<>();

        for (long i = 0; i < SIZE_OF_DATA; i+=INTERVAL_WITH_ID) {
            members.add(new Member(getLong(), create(), Role.valueOf(getRandomStringRole()), create(), getInt(MAX_LEVEL)));
        }
        return members;
    }
}
