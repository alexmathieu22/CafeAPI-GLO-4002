package ca.ulaval.glo4002.cafe.application.dto;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.valueobjects.CafeName;

public record LayoutDTO(CafeName name, List<Cube> cubes) {
    public static LayoutDTO fromCafe(Cafe cafe) {
        return new LayoutDTO(cafe.getName(), cafe.getLayout().getCubes());
    }
}
