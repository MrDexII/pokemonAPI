package com.andrzej.RESTfullPokemonAPI.model;

import java.util.Objects;

public class PokemonStats {

    private Stats hp;
    private Stats attack;
    private Stats defence;
    private Stats specialAttack;
    private Stats specialDefence;
    private Stats speed;

    public PokemonStats() {
    }

    public PokemonStats(Stats hp, Stats attack, Stats defence, Stats specialAttack, Stats specialDefence, Stats speed) {
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.specialAttack = specialAttack;
        this.specialDefence = specialDefence;
        this.speed = speed;
    }

    public Stats getHp() {
        return hp;
    }

    public void setHp(Stats hp) {
        this.hp = hp;
    }

    public Stats getAttack() {
        return attack;
    }

    public void setAttack(Stats attack) {
        this.attack = attack;
    }

    public Stats getDefence() {
        return defence;
    }

    public void setDefence(Stats defence) {
        this.defence = defence;
    }

    public Stats getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(Stats specialAttack) {
        this.specialAttack = specialAttack;
    }

    public Stats getSpecialDefence() {
        return specialDefence;
    }

    public void setSpecialDefence(Stats specialDefence) {
        this.specialDefence = specialDefence;
    }

    public Stats getSpeed() {
        return speed;
    }

    public void setSpeed(Stats speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "PokemonStats{" +
                "hp=" + hp +
                ", attack=" + attack +
                ", defence=" + defence +
                ", specialAttack=" + specialAttack +
                ", specialDefence=" + specialDefence +
                ", speed=" + speed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonStats that = (PokemonStats) o;
        return Objects.equals(hp, that.hp) && Objects.equals(attack, that.attack) && Objects.equals(defence, that.defence) && Objects.equals(specialAttack, that.specialAttack) && Objects.equals(specialDefence, that.specialDefence) && Objects.equals(speed, that.speed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hp, attack, defence, specialAttack, specialDefence, speed);
    }
}
