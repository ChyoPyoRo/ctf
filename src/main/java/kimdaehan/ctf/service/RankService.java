package kimdaehan.ctf.service;

import kimdaehan.ctf.dto.RankGraphDTO;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    public void upsertRank(UserRank rank){
        rankRepository.save(rank);
    };

    public List<RankGraphDTO> getRankGraphByAffiliation(String affiliation){
        return rankRepository.findRankGraphByAffiliation(affiliation);
    }

    public List<RankGraphDTO> getRankListByUser(String userId){
        return rankRepository.findRankGraphByUserId(userId);
    }
}
