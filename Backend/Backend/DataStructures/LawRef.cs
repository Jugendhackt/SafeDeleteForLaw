namespace DataStructures {
public class LawRef {
	public string shorthand;
	public string paragraph;
	public string? subparagraph;
	public override string ToString() => subparagraph is null?$"{paragraph} {shorthand}":$"{paragraph} ({subparagraph}) {shorthand}";
}
}