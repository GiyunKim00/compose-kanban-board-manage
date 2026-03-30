package woowacourse.kanban.domain.card

sealed class TitleValidationResult {
    abstract val isValid: Boolean

    data object Valid : TitleValidationResult() {
        override val isValid: Boolean = true
    }

    data object Blank : TitleValidationResult() {
        override val isValid: Boolean = false
    }
}

sealed class TagValidationResult {
    abstract val isValid: Boolean

    data object Valid : TagValidationResult() {
        override val isValid: Boolean = true
    }

    data object TooManyTags : TagValidationResult() {
        override val isValid: Boolean = false
    }

    data object TooLongTag : TagValidationResult() {
        override val isValid: Boolean = false
    }

    data object InvalidBlankTag : TagValidationResult() {
        override val isValid: Boolean = false
    }
}