/*
 * Create a graphviz graph based on the classes in the specified java
 * source files.
 *
 * (C) Copyright 2002-2005 Diomidis Spinellis
 *
 * Permission to use, copy, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in
 * supporting documentation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * $Id$
 *
 */

package gr.spinellis.umlgraph.doclet;

import com.sun.javadoc.*;

import java.io.*;
import java.util.*;

/**
 * Doclet API implementation
 * @version $Revision$
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class UmlGraph {
    /** Entry point */
    public static boolean start(RootDoc root) throws IOException {
	Options opt = new Options();
	ClassInfo.reset();

	opt.setOptions(root.options());
	opt.openFile();
	opt.setOptions(root.classNamed("UMLOptions"));
	prologue(opt);
	ClassDoc[] classes = root.classes();

	ClassGraph c = new ClassGraph(root.specifiedPackages(),
	    opt.apiDocRoot, opt.apiDocMapFileName);
	for (ClassDoc cd : classes) {
	    // Process class-local options (through @opt tags)
	    Options localOpt = (Options) opt.clone();
	    localOpt.setOptions(cd);
	    c.printClass(localOpt, cd);
	}
	for (ClassDoc cd : classes) {
	    // Process class-local options (through @opt tags)
	    Options localOpt = (Options) opt.clone();
	    localOpt.setOptions(cd);
	    c.printRelations(localOpt, cd);
	}

	c.printExtraClasses(opt, root);
	epilogue(opt);
	return true;
    }

    /** Option checking */
    public static int optionLength(String option) {
	if(option.equals("-qualify") ||
	   option.equals("-horizontal") ||
	   option.equals("-attributes") ||
	   option.equals("-operations") ||
	   option.equals("-constructors") ||
	   option.equals("-visibility") ||
	   option.equals("-types") ||
	   option.equals("-all") ||
	   option.equals("-noguillemot"))
	    return 1;
	else if(option.equals("-nodefillcolor") ||
	   option.equals("-nodefontcolor") ||
	   option.equals("-nodefontsize") ||
	   option.equals("-nodefontname") ||
	   option.equals("-nodefontabstractname") ||
	   option.equals("-edgefontcolor") ||
	   option.equals("-edgecolor") ||
	   option.equals("-edgefontsize") ||
	   option.equals("-edgefontname") ||
	   option.equals("-output") ||
	   option.equals("-outputencoding") ||
	   option.equals("-bgcolor") ||
	   option.equals("-hide") ||
	   option.equals("-apidocroot") ||
	   option.equals("-apidocmap"))
	    return 2;
	else
	    return 0;
    }

    /** Indicate the language version we support */
    public static LanguageVersion languageVersion() {
	return LanguageVersion.JAVA_1_5;
    }

    /** Dot prologue */
    private static void prologue(Options opt) {
	opt.w.println(
	    "#!/usr/local/bin/dot\n" +
	    "#\n" +
	    "# Class diagram \n" +
	    "# Generated by UmlGraph version " +
	    Version.VERSION + " (http://www.spinellis.gr/sw/umlgraph)\n" +
	    "#\n\n" +
	    "digraph G {\n" +
	    "\tedge [fontname=\"" + opt.edgeFontName +
	    "\",fontsize=10,labelfontname=\"" + opt.edgeFontName +
	    "\",labelfontsize=10];\n" +
	    "\tnode [fontname=\"" + opt.nodeFontName +
	    "\",fontsize=10,shape=record];"
	);
	if (opt.horizontal)
	    opt.w.println("\trankdir=LR;\n\tranksep=1;");
	if (opt.bgColor != null)
	    opt.w.println("\tbgcolor=\"" + opt.bgColor + "\";\n");
    }

    /** Dot epilogue */
    private static void epilogue(Options opt) {
	opt.w.println("}\n");
	opt.w.flush();
    }
}
