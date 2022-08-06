package marsh.thomas.redditkmm.common.util

fun interface ValidUrlUseCase {
    operator fun invoke(pattern: String): Boolean
}

class FakeValidUrlUseCase : ValidUrlUseCase {
    private var valid: Boolean = false

    fun setValid(valid: Boolean) {
        this.valid = valid
    }

    override fun invoke(pattern: String) = valid
}