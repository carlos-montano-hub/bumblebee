from fastapi import APIRouter

router = APIRouter(prefix="/items", tags=["Items"])


@router.get("/")
async def list_items():
    return [{"id": 1, "name": "Item 1"}]


@router.get("/{item_id}")
async def get_item(item_id: int):
    return {"id": item_id, "name": f"Item {item_id}"}
