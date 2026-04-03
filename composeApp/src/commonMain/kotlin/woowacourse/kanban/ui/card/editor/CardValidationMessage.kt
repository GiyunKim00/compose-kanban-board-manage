package woowacourse.kanban.ui.card.editor

import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult

fun TitleValidationResult.message(): String = when (this) {
    TitleValidationResult.Valid -> ""
    TitleValidationResult.Blank -> "제목을 입력해 주세요."
}

fun TagValidationResult.message(): String = when (this) {
    TagValidationResult.Valid -> "5자 이내의 태그를 최대 5개까지 등록할 수 있습니다."
    TagValidationResult.InvalidBlankTag -> "빈 태그는 입력할 수 없습니다."
    TagValidationResult.TooManyTags -> "태그는 최대 5개까지 입력할 수 있습니다."
    TagValidationResult.TooLongTag -> "태그는 5자 이내로 입력해 주세요."
}