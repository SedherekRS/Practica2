package unam.tic.pokemon.Modelo;

public class Pokemon {
    private long id;
    private String sprite;
    private String name;
    private String type1;
    private String type2;
    private String urlPokemon;

    public Pokemon(long id, String sprite, String name, String type1, String type2) {
        this.id = id;
        this.sprite = sprite;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
    }

    public Pokemon( String name) {
        this.name = name;
    }

    public String getUrlPokemon() {
        return urlPokemon;
    }

    public void setUrlPokemon(String urlPokemon) {
        this.urlPokemon = urlPokemon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }
}
