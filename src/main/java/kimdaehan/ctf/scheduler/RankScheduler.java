package kimdaehan.ctf.scheduler;

import kimdaehan.ctf.dto.UserPageDTO;
import kimdaehan.ctf.entity.AffiliationKey;
import kimdaehan.ctf.entity.UserRank;
import kimdaehan.ctf.entity.RecordKey;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.service.RankService;
import kimdaehan.ctf.service.ServerSettingService;
import kimdaehan.ctf.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class RankScheduler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final RankService rankService;
    private final ServerSettingService serverSettingService;
    @Async
    @Scheduled(cron = "0 0 * * * *") // 정각에 실행
    public void userRankHourSave(){
        LocalDateTime serverStartTime = serverSettingService.getServerStart();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);

        if(serverStartTime.isBefore(localDateTime)){
            List<UserPageDTO> nbList = userService.getRankAndScoreUsersByAffiliation("NB");
            List<UserPageDTO> ybList = userService.getRankAndScoreUsersByAffiliation("YB");
            List<UserPageDTO> schList = userService.getRankAndScoreUsersByAffiliation("SCH");
            List<UserPageDTO> allList = userService.getAllUserList();
            int nbRank = 1;
            int ybRank = 1;
            int schRank = 1;
            int allRank = 1;

            logger.info("------- NB RankScheduler Start -------");
            for (UserPageDTO nb : nbList){
                User user = userService.getUserId(nb.getUser_id());
                UserRank rank = UserRank.builder()
                        .affiliationKey(new AffiliationKey(user, localDateTime, User.Affiliation.NB))
                        .score(Math.toIntExact(nb.getTotal_score()))
                        .userRank(nbRank)
                        .build();
                rankService.upsertRank(rank);
                nbRank += 1;
            }
            logger.info("------- YB RankScheduler Start -------");
            for (UserPageDTO yb : ybList){
                User user = userService.getUserId(yb.getUser_id());
                UserRank rank = UserRank.builder()
                        .affiliationKey(new AffiliationKey(user, localDateTime, User.Affiliation.YB))
                        .score(Math.toIntExact(yb.getTotal_score()))
                        .userRank(ybRank)
                        .build();
                rankService.upsertRank(rank);
                ybRank += 1;
            }

            logger.info("------- SCH RankScheduler Start -------");
            for (UserPageDTO sch : schList){
                User user = userService.getUserId(sch.getUser_id());
                UserRank rank = UserRank.builder()
                        .affiliationKey(new AffiliationKey(user, localDateTime, User.Affiliation.SCH))
                        .score(Math.toIntExact(sch.getTotal_score()))
                        .userRank(schRank)
                        .build();
                rankService.upsertRank(rank);
                schRank += 1;
            }

            logger.info("------- ALL RankScheduler Start -------");
            for (UserPageDTO all : allList){
                User user = userService.getUserId(all.getUser_id());
                UserRank rank = UserRank.builder()
                        .affiliationKey(new AffiliationKey(user, localDateTime, User.Affiliation.ALL))
                        .score(Math.toIntExact(all.getTotal_score()))
                        .userRank(allRank)
                        .build();
                rankService.upsertRank(rank);
                allRank += 1;
            }
        }
    }


}
