package org.lbogdanov.poker.web.plugin;

import java.util.Arrays;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryPluginResourceReference;

import com.google.common.collect.Iterables;

/**
 * A <code>ResourceRefernece</code> for JQuery simple slider.
 * 
 * @author Alexandra Fomina
 */
public class SimpleSliderPlugin extends JQueryPluginResourceReference {

    private static final SimpleSliderPlugin INSTANCE = new SimpleSliderPlugin();
    private static final ResourceReference CSS = new CssResourceReference(SimpleSliderPlugin.class, "simple-slider.css");
    private static final ResourceReference VOLUME_THEME = new CssResourceReference(SimpleSliderPlugin.class,
                                                          "simple-slider-volume.css");

    /**
     * Returns a single <code>SimpleSliderPlugin</code> instance.
     * 
     * @return the instance
     */
    public static SimpleSliderPlugin get() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        return Iterables.concat(super.getDependencies(), Arrays.asList(CssHeaderItem.forReference(CSS),
                                                         CssHeaderItem.forReference(VOLUME_THEME)));
    }

    private SimpleSliderPlugin() {
        super(SimpleSliderPlugin.class, "simple-slider.js");
    }

}
