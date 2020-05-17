package md.jcore.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JComponent;

import md.jcore.awt.AdvancedGraphics;
import md.jcore.geom.MDOrthogonalProjector3D;
import md.jcore.geom.MDProjector;
import md.jcore.math.MDVector;

public class ProjectorComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private final TreeSet<MDVector.Double> points;
	private MDProjector.Double projector;

	public ProjectorComponent() {
		this.points = new TreeSet<>();
		this.projector = new MDOrthogonalProjector3D.Double();
	}
	
	public ProjectorComponent(MDProjector.Double projector) {
		this.points = new TreeSet<>();
		this.setProjector(projector);
	}
	
	public ProjectorComponent(Collection<MDVector.Double> points, MDProjector.Double projector) {
		this(projector);
		this.points.addAll(points);
	}

	public TreeSet<MDVector.Double> pointSet() {
		return points;
	}
	
	public MDProjector.Double getProjector() {
		return projector;
	}

	public void setProjector(MDProjector.Double projector) {
		if (projector == null)
			throw new NullPointerException("Projector cannot be set to null");
		this.projector = projector;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AdvancedGraphics gadv = new AdvancedGraphics(g2d);
		
		gadv.fillRectangle(getBackground(), new Rectangle(new Point(), getSize()));

		for (MDVector.Double v : points) {
			MDVector.Double v2d = projector.project(v);

			double px = getWidth() / 2.0 + v2d.get(0);
			double py = getHeight() / 2.0 - v2d.get(1);

			gadv.drawLine(getForeground(), new BasicStroke(2), new Line2D.Double(px, py - 2, px, py + 2));
			gadv.drawLine(getForeground(), new BasicStroke(2), new Line2D.Double(px - 2, py, px + 2, py));
			gadv.drawText(getForeground(), getFont(), v2d.toString(), new Point2D.Double(px, py), AdvancedGraphics.TEXT_ALIGN_LEFT, AdvancedGraphics.TEXT_ALIGN_CENTER);
		}
	}
}
