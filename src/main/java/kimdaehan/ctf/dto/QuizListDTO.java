package kimdaehan.ctf.dto;

import kimdaehan.ctf.entity.Quiz;

import java.util.UUID;

public interface QuizListDTO {
    String getQuizId();
    String getQuizName();
    String getAuthor();
    String getCategory();
    Integer getScore();
    String getTest();

    default UUID getQuizIdAsUuid(){
        return UUID.fromString(
                getQuizId()
                        .toLowerCase()
                        .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                                "$1-$2-$3-$4-$5"
                        )
        );
    }
    default Quiz.CategoryType getCategoryByEnum() {
        if(getCategory().equals("reversing")){
            return Quiz.CategoryType.REVERSING;
        }else if(getCategory().equals("web")){
            return Quiz.CategoryType.WEB;
        }else if(getCategory().equals("pwn")){
            return Quiz.CategoryType.PWN;
        }else if(getCategory().equals("forensics")){
            return Quiz.CategoryType.FORENSICS;
        }else {
            return Quiz.CategoryType.MISC;
        }
    }
}
