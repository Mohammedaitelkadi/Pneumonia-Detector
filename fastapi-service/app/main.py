from fastapi import FastAPI, HTTPException, UploadFile, File
from pydantic import BaseModel
from PIL import Image
from ultralytics import YOLO
import numpy as np
import os
import io

app = FastAPI(title="Pneumonia YOLOv8 Service")

class AnalyzeRequest(BaseModel):
    file_path: str

class AnalyzeResponse(BaseModel):
    ai_prediction: str
    ai_confidence: float

MODEL_PATH = os.getenv("MODEL_PATH", "app/models/yolov8_pneumonia.pt")
model = None

@app.on_event("startup")
def load_model():
    global model
    try:
        model = YOLO(MODEL_PATH)
    except Exception as e:
        model = None

@app.get("/health")
def health():
    return {"status": "ok", "model_loaded": model is not None}

def predict_with_yolo(img: Image.Image) -> AnalyzeResponse:
    if not model:
        raise HTTPException(status_code=500, detail="Model not loaded")
    res = model.predict(img, verbose=False)[0]
    names = model.names if hasattr(model, "names") else {0: "NORMAL", 1: "PNEUMONIA"}

    if hasattr(res, "probs") and res.probs is not None:
        confs = res.probs.data.tolist()
        idx = int(np.argmax(confs))
        label = names.get(idx, str(idx)).upper()
        conf = float(confs[idx])
    else:
        has_boxes = (res.boxes is not None) and (len(res.boxes) > 0)
        label = "PNEUMONIA" if has_boxes else "NORMAL"
        conf = float(res.boxes.conf.max().item()) if has_boxes else 0.50

    return AnalyzeResponse(ai_prediction=label, ai_confidence=round(conf, 4))


@app.post("/analyze", response_model=AnalyzeResponse)
def analyze(req: AnalyzeRequest):
    if not os.path.exists(req.file_path):
        raise HTTPException(status_code=400, detail="file_path not found")
    try:
        img = Image.open(req.file_path).convert("RGB")
        return predict_with_yolo(img)
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# @app.post("/analyze-upload", response_model=AnalyzeResponse)
# async def analyze_upload(file: UploadFile = File(...)):
#     try:
#         content = await file.read()
#         img = Image.open(io.BytesIO(content)).convert("RGB")
#         return predict_with_yolo(img)
#     except HTTPException:
#         raise
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=str(e))