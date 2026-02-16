import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		// Your code goes here
        String window = "";
        char c;

        In in = new In(fileName);
        for (int i = 0; i < this.windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }

        while(!in.isEmpty()) {
            c = in.readChar();
            List probs = this.CharDataMap.get(window);
            // In case this window isn't in the hashmap yet we need to add it.
            if (null == probs) {
                probs = new List();
                this.CharDataMap.put(window, probs);
            }
            probs.update(c);
            // Effectively deletes the first character and we advance to the next char.
            window = window.substring(1) + c;
        }

        for(List probs: this.CharDataMap.values()) {
            this.calculateProbabilities(probs);
        }

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {
        int totalChars = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            totalChars += probs.get(i).count;
        }

        double cumulativeProbability = 0.0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentData = probs.get(i);
            // According to the algorithm, the probability is simple, the cumulative is added as we go.
            currentData.p = (double)(currentData.count) / totalChars;
            cumulativeProbability += currentData.p;
            currentData.cp  = cumulativeProbability;
        }
		// Your code goes here
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		// Your code goes here
        double rand = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentData = probs.get(i);
            if (currentData.cp > rand) {
                return currentData.chr;
            }
        }
        // In case we didn't find anything, according to the instructions we return a space character.
		return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size textLength
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param textLength - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		// Your code goes here
        // As in instructions, we need to terminate in this edge-case scenario.
        if (this.windowLength > initialText.length()) {
            return initialText;
        }
        String generatedText = initialText;
        String newWindow = initialText.substring(initialText.length() - this.windowLength);
        while(generatedText.length() < initialText.length() + textLength) {
            List probs = this.CharDataMap.get(newWindow);
            // There was no occurrence of this specific window, so we succeeded and returned first attempt.
            if (null == probs) {
                return generatedText;
            }
            // We now continue in the window, with the new random character, and we need to slide it a bit to the right when slicing it.
            char randomChar = this.getRandomChar(probs);
            generatedText += randomChar;
            newWindow = generatedText.substring(generatedText.length() - this.windowLength);
        }

        return generatedText;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
        // Just same as was in the document, just prettified a bit.
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        final int HARDCODED_SEED = 20;

        LanguageModel lm;
        if (randomGeneration) {
            lm = new LanguageModel(windowLength);
        }
        else {
            lm = new LanguageModel(windowLength, HARDCODED_SEED);
        }

        lm.train(fileName);

        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
