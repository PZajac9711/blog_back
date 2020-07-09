package pl.zajac.model.coverter;

public interface Converter<T,F>{
    T convert(F from);
}
