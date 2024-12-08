package com.tinuproject.tinu.domain.enum

enum class Social(var code:Int, var company : String) {
    KAKAO(0, "카카오"),
    GOOGLE(1, "구글"),
    NAVER(2, "네이버");


    companion object{
        fun getSocial(provider : String) : Social{
            if(provider == "kakao") return KAKAO
            else  return NAVER
        }
    }

}