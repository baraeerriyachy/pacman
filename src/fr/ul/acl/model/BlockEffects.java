package fr.ul.acl.model;

import java.awt.Point;

public record BlockEffects(int damage, Point destination, boolean exit, Trigger trigger) {}