package woowacourse.kanban.domain.card

object CardValidator {
    private const val MAX_TAG_COUNT = 5
    private const val MAX_TAG_LENGTH = 5

    fun validateTitle(rawText: String): TitleValidationResult {
        return if (rawText.trim().isNotBlank()) {
            TitleValidationResult.Valid
        } else {
            TitleValidationResult.Blank
        }
    }

    fun parseTags(rawText: String): List<String> {
        if (rawText.isBlank()) return emptyList()
        return rawText.split(",").map { it.trim() }
    }

    fun normalizeTags(tags: List<String>): List<String> {
        return tags
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    fun validateTags(rawText: String): TagValidationResult {
        if (rawText.isBlank()) return TagValidationResult.Valid
        return validateTags(parseTags(rawText))
    }

    fun validateTags(tags: List<String>): TagValidationResult {
        val normalizedTags = tags.map { it.trim() }

        if (normalizedTags.any { it.isBlank() }) {
            return TagValidationResult.InvalidBlankTag
        }

        if (normalizedTags.size > MAX_TAG_COUNT) {
            return TagValidationResult.TooManyTags
        }

        if (normalizedTags.any { it.length > MAX_TAG_LENGTH }) {
            return TagValidationResult.TooLongTag
        }

        return TagValidationResult.Valid
    }
}