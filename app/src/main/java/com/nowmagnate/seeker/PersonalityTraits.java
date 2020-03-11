package com.nowmagnate.seeker;

public class PersonalityTraits {

    private String trait;
    private Boolean isChosen;

    public PersonalityTraits(String trait, Boolean isChosen) {
        this.trait = trait;
        this.isChosen = isChosen;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public Boolean getChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }
}
