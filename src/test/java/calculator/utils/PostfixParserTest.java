package calculator.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PostfixParserTest {

    private static Stream<Arguments> provideNormalExpression() {
        return Stream.of(
            Arguments.of("1 + 3 / 2", Arrays.asList("1", "3", "2", "/", "+")),
            Arguments.of("1 / 5 / 3", Arrays.asList("1", "5", "/", "3", "/")),
            Arguments.of("1 * 3 + 5 / 2", Arrays.asList("1", "3", "*", "5", "2", "/", "+")),
            Arguments.of("1.5 * 3.5 + 5.5 / 2.012", Arrays.asList("1.5", "3.5", "*", "5.5", "2.012", "/", "+")),
            Arguments.of("-1.5 * 3.5 + -5.5 / 2.012", Arrays.asList("-1.5", "3.5", "*", "-5.5", "2.012", "/", "+"))
        );
    }

    private static Stream<Arguments> provideWrongExpression() { // argument source method
        return Stream.of(
            Arguments.of("1 + 3 / 2 ㅁ", Arrays.asList("1", "3", "2", "/", "+")),
            Arguments.of("1 / 5 // 3 ", Arrays.asList("1", "5", "/", "3", "/")),
            Arguments.of("1 * 3 ++ 5 / 2", Arrays.asList("1", "3", "*", "5", "2", "/", "+")),
            Arguments.of("1.5 * 3.5 + 5.5 / 2.012ㅇㄴㅁ", Arrays.asList("1.5", "3.5", "*", "5.5", "2.012", "/", "+"))
        );
    }

    @DisplayName("입력받은 식을 후위표기식으로 변환한다.")
    @ParameterizedTest
    @MethodSource("provideNormalExpression")
    void parse_expression_to_postfix(String input, List<String> expected) {
        assertThat(PostfixParser.parse(input)).isEqualTo(expected);
    }

    @DisplayName("입력된 값이 공백이거나 null이면 예외를 던진다.")
    @ParameterizedTest
    @NullAndEmptySource
    void parse_blankOrNull_throwException(String input) {
        assertThatThrownBy(() -> PostfixParser.parse(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 정상적인 입력이 아닙니다. 다시 입력해주세요.");
    }

    @DisplayName("입력된 값이 잘못된 식이면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"1 + 3 / 2 ㅁ", "1 / 5 // 3 ", "1 * 3 ++ 5 / 2", "1.5 * 3.5 + 5.5 / 2.012ㅇㄴㅁ", "1+2+3",
        "1 + ! + 3"})
    void parse_WrongInput_ThrowException(String input) {
        assertThatThrownBy(() -> PostfixParser.parse(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 정상적인 입력이 아닙니다. 다시 입력해주세요.");
    }
}
