package checker.tbu;

public final class FoundTextOnWebPageTheFuture implements checker.tbu.FoundTextOnWebPage {

    private final String body;
    private final String textToFind;

    public FoundTextOnWebPageTheFuture(String body, String textToFind) {
        this.body = body;
        this.textToFind = textToFind;
    }

    @Override
    public boolean textExists() {
        return body.contains(textToFind);
    }
}
