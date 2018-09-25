package ru.pva.code.style.checks;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.FINAL;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_RETURN;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_THROW;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.SLIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessages;
import lombok.val;
import org.mockito.ArgumentCaptor;
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
	@Mock
	private FileContents fileContents;
	@Mock
	private LocalizedMessages localizedMessages;

	private DefaultConfiguration configuration;

	@InjectMocks
	private EmptyLineBeforeReturnCheck tested;

	@BeforeMethod
	public void before() throws CheckstyleException {
		tested = null;
		initMocks(this);

		tested.setMessages(localizedMessages);
		tested.setFileContents(fileContents);
		tested.configure(new DefaultConfiguration("any"));
	}

	@Test
	public void getDefaultTokens() {
		val actual = tested.getDefaultTokens();

		assertThat(actual).hasSize(2);
		assertThat(actual).contains(LITERAL_RETURN, LITERAL_THROW);
	}

	@Test
	public void getAcceptableTokens() {
		val actual = tested.getAcceptableTokens();

		assertThat(actual).hasSize(3);
		assertThat(actual).contains(SLIST, LITERAL_RETURN, LITERAL_THROW);
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
		verifyNoMoreInteractions(returnToken, parentToken, localizedMessages, fileContents);
	}

	@Test
	public void nothingDoingWhenParentTokenIsNotBlock() {
		when(returnToken.getParent()).thenReturn(parentToken);
		when(parentToken.getType()).thenReturn(FINAL);

		tested.visitToken(returnToken);

		verify(returnToken).getParent();
		verify(parentToken).getType();
		verifyNoMoreInteractions(returnToken, parentToken, localizedMessages, fileContents);
	}

	@Test
	public void checkIsOkWhenParentBlockHasLessThanThreeLines() {
		when(returnToken.getParent()).thenReturn(parentToken);
		when(parentToken.getType()).thenReturn(SLIST);
		when(parentToken.getLineNo()).thenReturn(10);
		when(returnToken.getLineNo()).thenReturn(12);

		tested.visitToken(returnToken);

		verify(returnToken).getParent();
		verify(parentToken).getType();
		verify(parentToken).getLineNo();
		verify(returnToken).getLineNo();
		verifyNoMoreInteractions(returnToken, parentToken, localizedMessages, fileContents);
	}

	@Test
	public void checkIsOkWhenReturnIsHaveEmptyLineBefore() {
		when(returnToken.getParent()).thenReturn(parentToken);
		when(parentToken.getType()).thenReturn(SLIST);
		when(parentToken.getLineNo()).thenReturn(1);
		when(returnToken.getLineNo()).thenReturn(4);
		when(fileContents.getLine(4-2)).thenReturn("");

		tested.visitToken(returnToken);

		verify(returnToken).getParent();
		verify(parentToken).getType();
		verify(parentToken).getLineNo();
		verify(returnToken).getLineNo();
		verify(fileContents).getLine(2);
		verifyNoMoreInteractions(returnToken, parentToken, localizedMessages, fileContents);
	}

	@Test
	public void checkLogFailWhenReturnIsHaveWhitespaceLineBefore() {
		final ArgumentCaptor<LocalizedMessage> localizedMessageCaptor = forClass(LocalizedMessage.class);
		val returnLineNo = 4;
		val returnColumnNo = 6;

		when(returnToken.getParent()).thenReturn(parentToken);
		when(parentToken.getType()).thenReturn(SLIST);
		when(parentToken.getLineNo()).thenReturn(1);
		when(returnToken.getLineNo()).thenReturn(returnLineNo);
		when(returnToken.getColumnNo()).thenReturn(returnColumnNo);
		when(fileContents.getLine(returnLineNo-2)).thenReturn("  ");
		when(fileContents.getLines()).thenReturn(new String[] {"{", "anyStringAnyString", "  ", "return 2;","}"});

		tested.visitToken(returnToken);

		val inOrder = inOrder(returnToken, parentToken, localizedMessages, fileContents);
		inOrder.verify(returnToken).getParent();
		inOrder.verify(parentToken).getType();
		inOrder.verify(parentToken).getLineNo();
		inOrder.verify(returnToken).getLineNo();
		inOrder.verify(fileContents).getLine(returnLineNo-2);
		inOrder.verify(returnToken).getLineNo();
		inOrder.verify(returnToken).getColumnNo();
		inOrder.verify(fileContents).getLines();
		inOrder.verify(localizedMessages).add(localizedMessageCaptor.capture());
		inOrder.verifyNoMoreInteractions();

		val actualErrorMessage = localizedMessageCaptor.getValue();

		assertThat(actualErrorMessage.getLineNo()).isEqualTo(returnLineNo);
		assertThat(actualErrorMessage.getColumnNo()).isEqualTo(returnColumnNo + 1);
		assertThat(actualErrorMessage.getMessage()).isEqualTo("Empty string must be before return");
		assertThat(actualErrorMessage.getSourceName()).isEqualTo("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck");
	}

	@Test
	public void checkLogFailWhenReturnIsHaveEmptyLineBefore() {
		final ArgumentCaptor<LocalizedMessage> localizedMessageCaptor = forClass(LocalizedMessage.class);
		val returnLineNo = 4;
		val returnColumnNo = 6;

		when(returnToken.getParent()).thenReturn(parentToken);
		when(parentToken.getType()).thenReturn(SLIST);
		when(parentToken.getLineNo()).thenReturn(1);
		when(returnToken.getLineNo()).thenReturn(returnLineNo);
		when(returnToken.getColumnNo()).thenReturn(returnColumnNo);
		when(fileContents.getLine(returnLineNo-2)).thenReturn("anyStringAnyString");
		when(fileContents.getLines()).thenReturn(new String[] {"{", "anyStringAnyString", "anyStringAnyString", "return 2;","}"});

		tested.visitToken(returnToken);

		val inOrder = inOrder(returnToken, parentToken, localizedMessages, fileContents);
		inOrder.verify(returnToken).getParent();
		inOrder.verify(parentToken).getType();
		inOrder.verify(parentToken).getLineNo();
		inOrder.verify(returnToken).getLineNo();
		inOrder.verify(fileContents).getLine(returnLineNo-2);
		inOrder.verify(returnToken).getLineNo();
		inOrder.verify(returnToken).getColumnNo();
		inOrder.verify(fileContents).getLines();
		inOrder.verify(localizedMessages).add(localizedMessageCaptor.capture());
		inOrder.verifyNoMoreInteractions();

		val actualErrorMessage = localizedMessageCaptor.getValue();

		assertThat(actualErrorMessage.getLineNo()).isEqualTo(returnLineNo);
		assertThat(actualErrorMessage.getColumnNo()).isEqualTo(returnColumnNo + 1);
		assertThat(actualErrorMessage.getMessage()).isEqualTo("Empty string must be before return");
		assertThat(actualErrorMessage.getSourceName()).isEqualTo("ru.pva.code.style.checks.EmptyLineBeforeReturnCheck");
	}
}