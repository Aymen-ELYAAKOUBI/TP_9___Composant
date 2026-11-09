$pdfPath = "C:/Users/user/OneDrive/Documents/TP Composant/TP_9___Composant.pdf"
$bytes = [System.IO.File]::ReadAllBytes($pdfPath)
$content = [System.Text.Encoding]::Default.GetString($bytes)

# Extract text inside (...) or after TJ/Tj operators or general readable text chunks
# Let's search for strings in parentheses
[regex]::Matches($content, '\((.*?)\)') | ForEach-Object {
    $_.Groups[1].Value
}
