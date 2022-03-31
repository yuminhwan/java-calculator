package calculator.view;

import java.util.List;

public interface OutputView {
    void printResult(String result);

    void printResults(List<String> results);
    
    void printWrongCommandError();

    void printErrorMsg(String msg);
}
