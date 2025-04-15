import os
import sys

from model2vec import StaticModel
from semhash import SemHash


filenames = []
threshold = 0.9
def read_text_files_from_directory(directory_path):
    text_contents = []
    for filename in os.listdir(directory_path):
        if filename.endswith('.txt'):
            filenames.append(filename)
            file_path = os.path.join(directory_path, filename)
            with open(file_path, 'r', encoding='utf-8') as file:
                content = file.read()
                text_contents.append(content)
    return text_contents

def process_text(directory_path):
    sys.stdout.reconfigure(encoding='utf-8')
    texts = read_text_files_from_directory(directory_path)
    #加载模型
    model = StaticModel.from_pretrained("data/m2vcn_model")
    #语料向量化、建立索引
    semhash = SemHash.from_records(records=texts, use_ann=True, model=model)
    vectors = semhash.index.vectors
    backend = semhash.index.backend
    #检索
    i = 0
    res = []
    for result in backend.threshold(vectors, threshold=1-threshold, max_k=5):
        # print(result)
        temp = f"{i + 1} {filenames[i]}: "
        for index, distance in zip(*result):
            if index > i:
                # temp += f"{filenames[index]} {round((1-distance)*100, 1)}%, "
                temp += filenames[index]+" "+str(round((1-distance)*100, 1))+"%, "
        if len(temp) > len(f"{i + 1} {filenames[i]}: "):  # 只输出有相似项的结果
            # print(temp)
            res.append(temp)
        i = i+1
    return res

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python test.py <file_path> <threshold>", file=sys.stderr)
        sys.exit(1)

    directory_path = sys.argv[1]
    if not os.path.isdir(directory_path):
        print(f"{directory_path} is not a directory.", file=sys.stderr)
        sys.exit(1)
    threshold = float(sys.argv[2])
    # directory_path = 'data/testdata/doccn'
    result = process_text(directory_path)
    # 循环遍历输出结果
    for i in range(len(result)):
        print(result[i])






