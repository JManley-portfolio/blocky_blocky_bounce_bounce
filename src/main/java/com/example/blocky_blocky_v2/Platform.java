package com.example.blocky_blocky_v2;

public class Platform {
    double x, y, width, height;
    double fallingSpeed = 0;

    public Platform(double x, double y, double width, double height) {
        this.x = x; // upper left corner
        this.y = y; // upper left corner
        this.width = width;
        this.height = height;
    }
}
