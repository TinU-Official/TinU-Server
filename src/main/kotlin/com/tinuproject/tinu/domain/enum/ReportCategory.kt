package com.tinuproject.tinu.domain.enum

enum class ReportCategory (var code : Int, var text : String, var isUser : Int){
    POST_SPAM(0,"스팸홍보/도배글입니다.",0),
    POST_PORN(1,"음란물입니다.",0),
    POST_NOT(2,"중고거래 게시글이 아닙니다.",0),
    POST_GUILTY(3,"불법정보를 포함하고 있습니다.",0),
    POST_DIRTY(4,"욕설/생명경시/혐오/차별적 표현을 포함한 게시물입니다.",0),
    POST_PRIVATE(5,"개인정보 노출 게시물입니다.",0),
    POST_ETC(6,"기타 부적절한 행위가 있습니다.",0),
    USER_BAD(7,"비매너 사용자입니다.",1),
    USER_NOT_PROMISE(8, "약속을 지키지않았습니다.",1),
    USER_SPAM(9,"사기로 보이는 행위를 합니다.",1),
    USER_NOT(10,"거래 목적 외의 대화를 합니다.",1),
    USER_EXPERT(11,"전문판매업자 같습니다.",1),
    USER_DIRTY(12,"욕설 - 비방 - 혐오표현을 합니다.",1),
    USER_ETC(13,"기타 부적절한 행위를 합니다.",1)
}