package util;

public record Pair<V1, V2>(V1 value1, V2 value2) {
    public static <V1, V2> Pair<V1, V2> of(V1 value1, V2 value2){
        return new Pair<>(value1, value2);
    }
}