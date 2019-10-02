package io.github.aquerr.koth.entity;

import com.flowpowered.math.vector.Vector3i;

public class Lobby
{
	private Vector3i firstPoint;
	private Vector3i secondPoint;
	private Vector3i spawnPoint;

	public Lobby(final Vector3i firstPoint, final Vector3i secondPoint, Vector3i spawnPoint)
	{
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.spawnPoint = spawnPoint;
	}

	public Vector3i getFirstPoint()
	{
		return this.firstPoint;
	}

	public Vector3i getSecondPoint()
	{
		return this.secondPoint;
	}

	public void setSpawnPoint(final Vector3i spawnPoint)
	{
		this.spawnPoint = spawnPoint;
	}

	public Vector3i getSpawnPoint()
	{
		return this.spawnPoint;
	}

	public boolean intersects(final Vector3i position)
	{
		boolean intersectX = false;
		boolean intersectY = false;
		boolean intersectZ = false;

		//Check X
		if (this.firstPoint.getX() <= this.secondPoint.getX() && (position.getX() <= this.secondPoint.getX() && position.getX() >= this.firstPoint.getX()))
		{
			intersectX = true;
		}
		else if (this.firstPoint.getX() >= this.secondPoint.getX() && (position.getX() <= this.firstPoint.getX() && position.getX() >= this.secondPoint.getX()))
		{
			intersectX = true;
		}

		//Check Y
		if (this.firstPoint.getY() < this.secondPoint.getY() && (position.getY() <= this.secondPoint.getY() && position.getY() >= this.firstPoint.getY()))
		{
			intersectY = true;
		}
		else if (this.firstPoint.getY() >= this.secondPoint.getY() && (position.getY() <= this.firstPoint.getY() && position.getY() >= this.secondPoint.getY()))
		{
			intersectY = true;
		}

		//Check Z
		if (this.firstPoint.getZ() <= this.secondPoint.getZ() && (position.getZ() <= this.secondPoint.getZ() && position.getZ() >= this.firstPoint.getZ()))
		{
			intersectZ = true;
		}
		else if (this.firstPoint.getZ() >= this.secondPoint.getZ() && (position.getZ() <= this.firstPoint.getZ() && position.getZ() >= this.secondPoint.getZ()))
		{
			intersectZ = true;
		}

		return intersectX && intersectY && intersectZ;
	}

}
