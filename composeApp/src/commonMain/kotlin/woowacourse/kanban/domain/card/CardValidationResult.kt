package woowacourse.kanban.domain.card

sealed class TitleValidationResult {
    object Valid : TitleValidationResult()
    object Blank : TitleValidationResult()
}

sealed class TagValidationResult {
    object Valid : TagValidationResult()
    object TooManyTags : TagValidationResult()
    object TooLongTag : TagValidationResult()
    object InvalidBlankTag : TagValidationResult()
}