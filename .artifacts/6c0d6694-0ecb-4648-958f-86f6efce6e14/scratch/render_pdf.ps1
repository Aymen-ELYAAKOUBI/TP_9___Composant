$pdfPath = "C:/Users/user/OneDrive/Documents/TP Composant/TP_9___Composant.pdf"
$outDir = "C:/Users/user/AndroidStudioProjects/TP_9___Composant/.artifacts/6c0d6694-0ecb-4648-958f-86f6efce6e14/scratch"

Add-Type -AssemblyName System.Runtime.WindowsRuntime
[Windows.Data.Pdf.PdfDocument,Windows.Data.Pdf,ContentType=WindowsRuntime] | Out-Null
[Windows.Storage.StorageFile,Windows.Storage,ContentType=WindowsRuntime] | Out-Null

$async = [Windows.Storage.StorageFile]::GetFileFromPathAsync($pdfPath)
$file = $async.AsTask().GetAwaiter().GetResult()

$async2 = [Windows.Data.Pdf.PdfDocument]::LoadFromFileAsync($file)
$pdfDoc = $async2.AsTask().GetAwaiter().GetResult()

Write-Host "PDF Page Count: $($pdfDoc.PageCount)"

for ($i = 0; $i -lt $pdfDoc.PageCount; $i++) {
    $page = $pdfDoc.GetPage($i)
    $outFilePath = Join-Path $outDir "page_$($i+1).png"
    $stream = [Windows.Storage.Streams.InMemoryRandomAccessStream]::new()

    $options = [Windows.Data.Pdf.PdfPageRenderOptions]::new()
    # Optional: set width/height or scaling

    $async3 = $page.RenderToStreamAsync($stream, $options)
    $async3.AsTask().GetAwaiter().GetResult()

    $decoder = [Windows.Graphics.Imaging.BitmapDecoder]::CreateAsync($stream).AsTask().GetAwaiter().GetResult()
    # Or just copy stream bytes to file
    $reader = [Windows.Storage.Streams.DataReader]::new($stream.GetInputStreamAt(0))
    $reader.LoadAsync($stream.Size).AsTask().GetAwaiter().GetResult()
    $bytes = New-Object byte[] $stream.Size
    $reader.ReadBytes($bytes)
    [System.IO.File]::WriteAllBytes($outFilePath, $bytes)
    Write-Host "Rendered page $($i+1) to $outFilePath"
    $page.Dispose()
}
