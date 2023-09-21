package kimdaehan.ctf.util;

import jakarta.annotation.Nullable;

public class Utility {

    public static boolean nullOrEmptyOrSpace(@Nullable String sentence){
        return (sentence == null || sentence.trim().isEmpty());
    }

}
