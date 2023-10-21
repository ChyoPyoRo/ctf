package kimdaehan.ctf.repository;

import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.entity.RecordKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<UserRank, RecordKey> {


}
