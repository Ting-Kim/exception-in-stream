package org.example.repository;

import org.example.model.Member;

import java.util.List;

public interface MemberRepository {

    void saveAll(List<Member> members);

    void levelUpBatchById(List<Long> memeberIds);
}