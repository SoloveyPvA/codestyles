package ru.pva.code.style.checks;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_RETURN;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.SLIST;
import static com.puppycrawl.tools.checkstyle.utils.CommonUtils.isBlank;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import lombok.val;


public class EmptyLineBeforeReturnCheck extends AbstractCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[]{
				LITERAL_RETURN
		};
	}

	@Override
	public int[] getAcceptableTokens() {
		return new int[]{
				SLIST,
				LITERAL_RETURN
		};
	}

	@Override
	public int[] getRequiredTokens() {
		return new int[0];
	}

	@Override
	public void visitToken(final DetailAST returnToken) {
		val block = returnToken.getParent();
		if (block == null || block.getType() != SLIST) {
			return;
		}

		val blockLineNumber = block.getLineNo();
		val returnLineNumber = returnToken.getLineNo();

		if ((returnLineNumber - blockLineNumber) < 3) {
			return;
		}

		if (!isThereEmptyLineBeforeReturn(returnLineNumber)) {
			log(returnToken.getLineNo(), returnToken.getColumnNo(), "Empty string must be before return");
		}
	}

	private boolean isThereEmptyLineBeforeReturn(final int returnLineNumber) {
		return isBlank(getLine(returnLineNumber - 1));
	}
}
