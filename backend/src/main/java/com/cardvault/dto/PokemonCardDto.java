package com.cardvault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCardDto {
    private String id;
    private String name;
    private String supertype;
    private List<String> subtypes;
    private String hp;
    private List<String> types;
    private String evolvesFrom;
    private List<Ability> abilities;
    private List<Attack> attacks;
    private List<Weakness> weaknesses;
    private List<Resistance> resistances;
    private List<String> retreatCost;
    private Integer convertedRetreatCost;
    private CardSet set;
    private String number;
    private String artist;
    private String rarity;
    private String flavorText;
    private List<Integer> nationalPokedexNumbers;
    private Legalities legalities;
    private CardImages images;
    private TcgPlayer tcgplayer;
    private Cardmarket cardmarket;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ability {
        private String name;
        private String text;
        private String type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attack {
        private String name;
        private List<String> cost;
        private Integer convertedEnergyCost;
        private String damage;
        private String text;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weakness {
        private String type;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Resistance {
        private String type;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardSet {
        private String id;
        private String name;
        private String series;
        private Integer printedTotal;
        private Integer total;
        private Legalities legalities;
        private String ptcgoCode;
        private String releaseDate;
        private String updatedAt;
        private CardImages images;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Legalities {
        private String unlimited;
        private String standard;
        private String expanded;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardImages {
        private String small;
        private String large;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TcgPlayer {
        private String url;
        private String updatedAt;
        private Prices prices;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Prices {
            private PriceDetail holofoil;
            private PriceDetail reverseHolofoil;
            private PriceDetail normal;
            @JsonProperty("1stEditionHolofoil")
            private PriceDetail firstEditionHolofoil;
            @JsonProperty("1stEditionNormal")
            private PriceDetail firstEditionNormal;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PriceDetail {
            private Double low;
            private Double mid;
            private Double high;
            private Double market;
            private Double directLow;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cardmarket {
        private String url;
        private String updatedAt;
        private Prices prices;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Prices {
            private Double averageSellPrice;
            private Double lowPrice;
            private Double trendPrice;
            private Double germanProLow;
            private Double suggestedPrice;
            private Double reverseHoloSell;
            private Double reverseHoloLow;
            private Double reverseHoloTrend;
            private Double lowPriceExPlus;
            private Double avg1;
            private Double avg7;
            private Double avg30;
            private Double reverseHoloAvg1;
            private Double reverseHoloAvg7;
            private Double reverseHoloAvg30;
        }
    }
}
