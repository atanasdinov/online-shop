package project.utils;

import org.modelmapper.PropertyMap;

public interface ModelParser {

    <S,D> D convert(S source, Class<D> destinationClass);
}
