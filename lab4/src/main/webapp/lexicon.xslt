<!-- lexiconList.xslt -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- Include menu.xslt -->
    <xsl:include href="menu.xslt"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
                <title>Lexicon List</title>
            </head>
            <body>
                <!-- Use the menu template -->
                <xsl:call-template name="menu"/>

                <h2>Lexicon List</h2>

                <table border="1">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Lexicon</th>
                            <th>Phrase</th>
                            <th>Translation</th>
                            <th>Explanation</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="//lexicon"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="lexicon">
        <tr>
            <td>
                <xsl:value-of select="lexiconId"/>
            </td>
            <td>
                <xsl:value-of select="text"/>
            </td>
            <td>
                <xsl:value-of select="phrase/phrase"/>
            </td>
            <td>
                <xsl:value-of select="semantics/translation"/>
            </td>
            <td>
                <xsl:value-of select="semantics/explanation"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
