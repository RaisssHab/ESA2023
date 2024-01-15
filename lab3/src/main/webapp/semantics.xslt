<!-- semantics.xslt -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" omit-xml-declaration="yes"/>

    <!-- Include menu.xslt -->
    <xsl:include href="menu.xslt"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Semantics List</title>
            </head>
            <body>
                <!-- Use the menu template -->
                <xsl:call-template name="menu"/>

                <h2>Semantics List</h2>

                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Translation</th>
                            <th>Explanation</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="//semantics"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="semantics">
        <tr>
            <td>
                <xsl:value-of select="semanticId"/>
            </td>
            <td>
                <xsl:value-of select="translation"/>
            </td>
            <td>
                <xsl:value-of select="explanation"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
