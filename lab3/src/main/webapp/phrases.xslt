<!-- phrases.xslt -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" omit-xml-declaration="yes"/>

    <!-- Include menu.xslt -->
    <xsl:include href="menu.xslt"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Phrases List</title>
            </head>
            <body>
                <!-- Use the menu template -->
                <xsl:call-template name="menu"/>

                <h2>Phrases List</h2>

                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Phrase</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="//phrase"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="phrase">
        <tr>
            <td>
                <xsl:value-of select="phraseId"/>
            </td>
            <td>
                <xsl:value-of select="phrase"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
