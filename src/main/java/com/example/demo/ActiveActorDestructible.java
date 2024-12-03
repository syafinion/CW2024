package com.example.demo;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	@Override
	public abstract void updatePosition();

	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	@Override
	public void destroy() {
		setDestroyed(true);
	}

	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public Bounds getAdjustedBounds() {
		Bounds originalBounds = this.getBoundsInParent();
		double widthShrinkFactor = 0.8; // Shrink width to 80% of the original
		double heightShrinkFactor = 0.4; // Shrink height to 60% of the original

		double width = originalBounds.getWidth() * widthShrinkFactor;
		double height = originalBounds.getHeight() * heightShrinkFactor;
		double x = originalBounds.getMinX() + (originalBounds.getWidth() - width) / 2;
		double y = originalBounds.getMinY() + (originalBounds.getHeight() - height) / 2;

		return new BoundingBox(x, y, width, height);
	}


	// TESTING
	// Temporary method to visualize the bounding box
	public Rectangle getBoundingBoxHighlight() {
		Bounds bounds = getAdjustedBounds();
		Rectangle highlight = new Rectangle(bounds.getWidth(), bounds.getHeight());
		highlight.setX(bounds.getMinX());
		highlight.setY(bounds.getMinY());
		highlight.setStroke(Color.RED);
		highlight.setFill(Color.TRANSPARENT);
		return highlight;
	}

	// Temporary method to update the bounding box highlight
	public void updateBoundingBoxHighlight(Rectangle highlight) {
		Bounds bounds = getAdjustedBounds();
		highlight.setX(bounds.getMinX());
		highlight.setY(bounds.getMinY());
		highlight.setWidth(bounds.getWidth());
		highlight.setHeight(bounds.getHeight());
	}



}
