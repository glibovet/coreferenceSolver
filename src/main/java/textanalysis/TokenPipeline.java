package textanalysis;

import textanalysis.pipeline.TokenBag;
import textanalysis.pipeline.StepHandler;
import textanalysis.pipeline.Token;

public class TokenPipeline {

    private StepHandler stepHandler;
    public TokenBag tokens;

    public int length = 0;
    public int currentIndex = 0;
    public int sentenceIndex = 0;

    private int skipCounter = 0;

    public TokenPipeline(TokenBag tokens, StepHandler r) {
        this.stepHandler = r;
        this.tokens = tokens;
        this.length = tokens.length();
    }

    public void start() {

        this.stepHandler.setPipeline(this);

        for (Token it : this.tokens.getTokens()) {

            if (this.skipCounter > 0) {
                this.skipCounter--;
                this.currentIndex++;
                continue;
            }

            this.stepHandler.setToken(it);
            if (this.currentIndex > 0) {
                this.stepHandler.setPrevToken(this.tokens.getTokens().get(this.currentIndex - 1));
            }
            this.stepHandler.handle();
            this.currentIndex++;

            String posTag = it.getToken().getPOSTag();
            if (posTag != null && posTag.equals("SENT_END")) {
                this.sentenceIndex++;
            }
            
            this.stepHandler.clear();

        }
    }

    public void skip(int tokens) {
        this.skipCounter = tokens;
    }

    public void skip() {
        this.skip(1);
    }

}
