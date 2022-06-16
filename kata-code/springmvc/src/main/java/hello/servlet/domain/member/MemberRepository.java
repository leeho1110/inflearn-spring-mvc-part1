package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 위 객체는 싱글톤 객체다.
 * 싱글톤 객체는 하나의 인스턴스가 모든 스레드에서 공유된다는 것을 의미하며, 당연하게도 동시성 문제가 고려되어 있지 않다.
 * 기본적으로는 싱글톤 객체에 상태를 갖는 필드를 선언하지 않는 편이 더 좋다.
 * 하지만 만약 필요하다면 실무에서는 ConcurrentHashMap, AtomicLong 사용을 고려해야 한다.
 *
 */
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    // singleton 생성시 private 생성자로 다른 곳에서 생성할 수 없도록 막아야함.
    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

}
