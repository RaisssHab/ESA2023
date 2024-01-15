<!-- menu.xslt -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template name="menu">
        <div id="menu">
            <a href="lexicon">Lexicon List</a>
            <a href="phrases">Phrases List</a>
            <a href="semantics">Semantics List</a>
        </div>
    </xsl:template>
</xsl:stylesheet>
