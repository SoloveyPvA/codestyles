package ru.pva.code.style.checks;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_RETURN;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.SLIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessages;
import lombok.val;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class EmptyLineBeforeReturnCheckTest {

	@Mock
	private DetailAST returnToken;
	@Mock
	private DetailAST parentToken;

	private FileContents fileContents;
	private LocalizedMessages localizedMessages;

	@InjectMocks
	private EmptyLineBeforeReturnCheck tested;

	@BeforeMethod
	public void before() {
		tested = null;
		initMocks(this);

		localizedMessages = new LocalizedMessages();
		tested.setMessages(localizedMessages);
	}

	@Test
	public void getDefaultTokens() {
		val actual = tested.getDefaultTokens();

		assertThat(actual).hasSize(1);
		assertThat(actual).contains(LITERAL_RETURN);
	}

	@Test
	public void getAcceptableTokens() {
		val actual = tested.getAcceptableTokens();

		assertThat(actual).hasSize(2);
		assertThat(actual).contains(SLIST, LITERAL_RETURN);
	}

	@Test
	public void getRequiredTokens() {
		val actual = tested.getRequiredTokens();

		assertThat(actual).isEmpty();
	}

	@Test
	public void nothingDoingWhenParentTokenIsNull() {
		when(returnToken.getParent()).thenReturn(null);

		tested.visitToken(returnToken);

		verify(returnToken).getParent();
		verifyNoMoreInteractions(returnToken, parentToken);

		assertThat(localizedMessages.getMessages()).isEmpty();
	}
}