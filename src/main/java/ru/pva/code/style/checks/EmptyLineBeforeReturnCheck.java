package ru.pva.code.style.checks;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.SLIST;
import static com.puppycrawl.tools.checkstyle.utils.CommonUtils.isBlank;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import lombok.val;


public class EmptyLineBeforeReturnCheck extends AbstractCheck {

	@Override
	public int[] getDefaultTokens() {
		return new int[]{
				TokenTypes.LITERAL_RETURN
		};
	}

	@Override
	public int[] getAcceptableTokens() {
		return new int[]{
				SLIST,
				TokenTypes.LITERAL_RETURN
		};
	}

	@Override
	public int[] getRequiredTokens() {
		return new int[0];
	}

	@Override
	public void visitToken(final DetailAST returnToken) {
		val block = returnToken.getParent();
		if (block.getType() != SLIST) {
			return;
		}

		val blockLineNumber = block.getLineNo();
		val returnLineNumber = returnToken.getLineNo();

		if ((returnLineNumber - blockLineNumber) < 3) {
			return;
		}

		if (!isThereEmptyLineBeforeReturn(returnLineNumber)) {
			log(returnLineNumber, returnToken.getColumnNo(), "Empty string must be return");
		}
	}

	private boolean isThereEmptyLineBeforeReturn(final int returnLineNumber) {
		return isBlank(getLine(returnLineNumber - 1));
	}
}
