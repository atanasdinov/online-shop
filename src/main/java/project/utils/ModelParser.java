package project.utils;

public interface ModelParser {

    <S,D> D convert(S source, Class<D> destinationClass);
}