from difflib import SequenceMatcher

# Define two code strings
code1 = """..."""
code2 = """..."""
similarity = SequenceMatcher(None, code1, code2).ratio() * 100
print(f"Similarity: {similarity:.2f}%")
