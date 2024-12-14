package com.esezak.client.UI.Elements.Panels;

import com.esezak.server.MovieLookup.Content.Review;

import java.util.ArrayList;

public class ReviewPanel extends SimplePanel{
    ArrayList<Review> reviews;
    public ReviewPanel(ArrayList<Review> reviews) {
        super();
        this.reviews = reviews;
    }
}
