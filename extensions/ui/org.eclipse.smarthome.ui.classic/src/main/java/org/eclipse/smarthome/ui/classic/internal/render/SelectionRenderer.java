/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.ui.classic.internal.render;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.smarthome.model.sitemap.Mapping;
import org.eclipse.smarthome.model.sitemap.Selection;
import org.eclipse.smarthome.model.sitemap.Widget;
import org.eclipse.smarthome.ui.classic.render.RenderException;
import org.eclipse.smarthome.ui.classic.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Selection widgets.
 *
 * @author Kai Kreuzer - Initial contribution and API
 *
 */
public class SelectionRenderer extends AbstractWidgetRenderer {

    @Override
    public boolean canRender(Widget w) {
        return w instanceof Selection;
    }

    @Override
    public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
        String snippet = getSnippet("selection");

        snippet = StringUtils.replace(snippet, "%category%", getCategory(w));
        snippet = StringUtils.replace(snippet, "%state%", getState(w));
        snippet = StringUtils.replace(snippet, "%format%", getFormat());

        String state = itemUIRegistry.getState(w).toString();
        Selection selection = (Selection) w;
        String mappedValue = "";

        StringBuilder rowSB = new StringBuilder();
        for (Mapping mapping : selection.getMappings()) {
            String rowSnippet = getSnippet("selection_row");
            String command = mapping.getCmd() != null ? mapping.getCmd() : "";
            rowSnippet = StringUtils.replace(rowSnippet, "%item%", w.getItem() != null ? w.getItem() : "");
            rowSnippet = StringUtils.replace(rowSnippet, "%cmd%", StringEscapeUtils.escapeHtml(command));
            rowSnippet = StringUtils.replace(rowSnippet, "%label%",
                    mapping.getLabel() != null ? StringEscapeUtils.escapeHtml(mapping.getLabel()) : "");
            if (state.equals(mapping.getCmd())) {
                rowSnippet = StringUtils.replace(rowSnippet, "%checked%", "checked=\"true\"");
                mappedValue = (mapping.getLabel() != null) ? mapping.getLabel() : mapping.getCmd();
            } else {
                rowSnippet = StringUtils.replace(rowSnippet, "%checked%", "");
            }
            rowSB.append(rowSnippet);
        }
        snippet = StringUtils.replace(snippet, "%label_header%", getLabel(w, mappedValue));
        snippet = StringUtils.replace(snippet, "%rows%", rowSB.toString());

        // Process the color tags
        snippet = processColor(w, snippet);

        sb.append(snippet);
        return null;
    }
}
