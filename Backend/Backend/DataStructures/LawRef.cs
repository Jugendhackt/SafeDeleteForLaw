namespace DataStructures {
public class LawRef {
	public string shorthand;
	public string paragraph;
	public string? subparagraph;
	public override string ToString() => subparagraph is null?$"{paragraph} {shorthand}":$"{paragraph} ({subparagraph}) {shorthand}";

	protected bool Equals(LawRef other) => string.Equals(shorthand, other.shorthand) && string.Equals(paragraph, other.paragraph) && string.Equals(subparagraph, other.subparagraph);

	public override bool Equals(object obj) {
		if (ReferenceEquals(null, obj)) return false;
		if (ReferenceEquals(this, obj)) return true;
		if (obj.GetType() != this.GetType()) return false;
		return Equals((LawRef) obj);
	}

	public override int GetHashCode() {
		unchecked {
			int hashCode = (shorthand != null ? shorthand.GetHashCode() : 0);
			hashCode = (hashCode * 397) ^ (paragraph != null ? paragraph.GetHashCode() : 0);
			hashCode = (hashCode * 397) ^ (subparagraph != null ? subparagraph.GetHashCode() : 0);
			return hashCode;
		}
	}

	public static bool operator ==(LawRef left, LawRef right) => Equals(left, right);

	public static bool operator !=(LawRef left, LawRef right) => !Equals(left, right);
}
}