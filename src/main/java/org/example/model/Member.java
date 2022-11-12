package org.example.model;

import lombok.Getter;

@Getter
public class Member extends BaseEntity {

    private Long id;
    private String name;
    private Role role;
    private String organization;
    private int level;

    protected Member() {
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", organization='" + organization + '\'' +
                ", level=" + level +
                '}';
    }

    public Member(Long id, String name, Role role, String organization, int level) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.organization = organization;
        this.level = level;
    }

    public Member levelUp() {
        if (level < 1) {
            throw new IllegalStateException("레벨업이 불가능한 레벨입니다.");
        }
        level++;
        return this;
    }

    public static Member of(Long id, MemberRq memberRq) throws RuntimeException {
        if (memberRq.getRole() == null) {
            throw new IllegalArgumentException("VALIDATION_ROLE viola");
        }
        return new Member(id,
                memberRq.getName(),
                Role.valueOf(memberRq.getRole()),
                memberRq.getOrganization(),
                memberRq.getLevel());
    }

    public static Member of(Long id, String name, Role role, String organization, int level) {
        if (role == null) {
            throw new IllegalArgumentException("VALIDATION_ROLE viola");
        }
        return new Member(id, name, role, organization, level);
    }
}
