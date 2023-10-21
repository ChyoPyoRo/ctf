package kimdaehan.ctf.service;

import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;


    public void upsertRank(UserRank rank){
        rankRepository.save(rank);
    };
}
